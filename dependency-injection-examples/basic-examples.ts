// Example implementations to accompany the dependency injection documentation

// Interfaces
interface ILogger {
  log(message: string): void;
}

interface IUserRepository {
  getById(id: number): User;
  save(user: User): boolean;
}

class User {
  constructor(public id: number, public name: string) {}
}

// Implementations
class ConsoleLogger implements ILogger {
  log(message: string): void {
    console.log(`[LOG]: ${message}`);
  }
}

class FileLogger implements ILogger {
  constructor(private filePath: string) {}
  
  log(message: string): void {
    console.log(`[FILE ${this.filePath}]: ${message}`);
    // In a real implementation, this would write to a file
  }
}

class SqlUserRepository implements IUserRepository {
  getById(id: number): User {
    console.log(`Getting user ${id} from SQL database`);
    // In a real implementation, this would query a database
    return new User(id, "User " + id);
  }
  
  save(user: User): boolean {
    console.log(`Saving user ${user.id} to SQL database`);
    // In a real implementation, this would save to a database
    return true;
  }
}

class InMemoryUserRepository implements IUserRepository {
  private users: Map<number, User> = new Map();
  
  getById(id: number): User {
    return this.users.get(id);
  }
  
  save(user: User): boolean {
    this.users.set(user.id, user);
    return true;
  }
}

// Example 1: No Dependency Injection (Anti-pattern)
class UserServiceWithoutDI {
  private repository = new SqlUserRepository(); // Hard dependency
  private logger = new ConsoleLogger();        // Hard dependency
  
  getUser(id: number): User {
    this.logger.log(`Fetching user ${id}`);
    return this.repository.getById(id);
  }
  
  saveUser(user: User): boolean {
    this.logger.log(`Saving user ${user.id}`);
    return this.repository.save(user);
  }
}

// Example 2: Constructor Injection
class UserServiceWithConstructorDI {
  private repository: IUserRepository;
  private logger: ILogger;
  
  constructor(repository: IUserRepository, logger: ILogger) {
    this.repository = repository;
    this.logger = logger;
  }
  
  getUser(id: number): User {
    this.logger.log(`Fetching user ${id}`);
    return this.repository.getById(id);
  }
  
  saveUser(user: User): boolean {
    this.logger.log(`Saving user ${user.id}`);
    return this.repository.save(user);
  }
}

// Example 3: Property/Setter Injection
class UserServiceWithPropertyDI {
  repository: IUserRepository;
  logger: ILogger;
  
  getUser(id: number): User {
    if (!this.repository || !this.logger) {
      throw new Error("Dependencies not set");
    }
    this.logger.log(`Fetching user ${id}`);
    return this.repository.getById(id);
  }
  
  saveUser(user: User): boolean {
    if (!this.repository || !this.logger) {
      throw new Error("Dependencies not set");
    }
    this.logger.log(`Saving user ${user.id}`);
    return this.repository.save(user);
  }
}

// Example 4: Method Injection
class UserServiceWithMethodDI {
  getUser(id: number, repository: IUserRepository, logger: ILogger): User {
    logger.log(`Fetching user ${id}`);
    return repository.getById(id);
  }
  
  saveUser(user: User, repository: IUserRepository, logger: ILogger): boolean {
    logger.log(`Saving user ${user.id}`);
    return repository.save(user);
  }
}

// Usage examples
function demonstrateBasicDI() {
  console.log("1. WITHOUT DI:");
  const serviceWithoutDI = new UserServiceWithoutDI();
  serviceWithoutDI.getUser(1);
  
  console.log("\n2. CONSTRUCTOR INJECTION:");
  const repo = new SqlUserRepository();
  const logger = new FileLogger("app.log");
  const serviceWithDI = new UserServiceWithConstructorDI(repo, logger);
  serviceWithDI.getUser(1);
  
  console.log("\n3. PROPERTY INJECTION:");
  const serviceWithPropertyDI = new UserServiceWithPropertyDI();
  serviceWithPropertyDI.repository = new InMemoryUserRepository();
  serviceWithPropertyDI.logger = new ConsoleLogger();
  serviceWithPropertyDI.getUser(1);
  
  console.log("\n4. METHOD INJECTION:");
  const serviceWithMethodDI = new UserServiceWithMethodDI();
  serviceWithMethodDI.getUser(1, new SqlUserRepository(), new ConsoleLogger());
}

demonstrateBasicDI();
