// Advanced DI container implementation with lifetime management and more

// Define lifetimes for registered services
enum Lifetime {
  Transient, // New instance each time
  Scoped,    // Same instance within a scope
  Singleton  // Same instance for the lifetime of container
}

// Registration info for a dependency
interface Registration {
  key: string;
  factory: (container: Container) => any;
  lifetime: Lifetime;
  implementation?: any;
}

// DI container scope for scoped services
class Scope {
  private instances = new Map<string, any>();
  
  getInstance<T>(key: string): T | undefined {
    return this.instances.get(key) as T;
  }
  
  setInstance<T>(key: string, instance: T): void {
    this.instances.set(key, instance);
  }
}

// Container implementation with advanced features
class Container {
  private registrations = new Map<string, Registration>();
  private singletonInstances = new Map<string, any>();
  private activeScope: Scope | null = null;
  
  /**
   * Register a service with the container
   */
  register<T>(key: string, factory: (container: Container) => T, lifetime: Lifetime = Lifetime.Transient): void {
    this.registrations.set(key, { key, factory, lifetime });
  }
  
  /**
   * Register a type directly (using its constructor)
   */
  registerType<T>(key: string, type: new (...args: any[]) => T, lifetime: Lifetime = Lifetime.Transient): void {
    this.registrations.set(key, {
      key,
      factory: () => new type(),
      lifetime,
      implementation: type
    });
  }
  
  /**
   * Register a constant value
   */
  registerInstance<T>(key: string, instance: T): void {
    this.singletonInstances.set(key, instance);
    this.registrations.set(key, {
      key,
      factory: () => instance,
      lifetime: Lifetime.Singleton
    });
  }
  
  /**
   * Resolve a dependency
   */
  resolve<T>(key: string): T {
    const registration = this.registrations.get(key);
    
    if (!registration) {
      throw new Error(`Dependency not registered: ${key}`);
    }
    
    // Handle different lifetimes
    switch (registration.lifetime) {
      case Lifetime.Singleton:
        if (!this.singletonInstances.has(key)) {
          this.singletonInstances.set(key, registration.factory(this));
        }
        return this.singletonInstances.get(key) as T;
        
      case Lifetime.Scoped:
        if (!this.activeScope) {
          throw new Error("Cannot resolve scoped service without an active scope");
        }
        
        let instance = this.activeScope.getInstance<T>(key);
        if (!instance) {
          instance = registration.factory(this);
          this.activeScope.setInstance(key, instance);
        }
        return instance;
        
      case Lifetime.Transient:
      default:
        return registration.factory(this) as T;
    }
  }
  
  /**
   * Create a new scope for scoped services
   */
  createScope(): Scope {
    return new Scope();
  }
  
  /**
   * Run a function within a scope
   */
  withScope<T>(fn: () => T): T {
    const previousScope = this.activeScope;
    this.activeScope = this.createScope();
    
    try {
      return fn();
    } finally {
      this.activeScope = previousScope;
    }
  }
}

// Example usage

// Interfaces
interface ILogger {
  log(message: string): void;
}

interface IUserRepository {
  getById(id: number): User;
}

interface IEmailService {
  sendEmail(to: string, subject: string, body: string): boolean;
}

// Implementations
class User {
  constructor(public id: number, public name: string, public email: string) {}
}

class ConsoleLogger implements ILogger {
  log(message: string): void {
    console.log(`[LOG]: ${message}`);
  }
}

class UserRepository implements IUserRepository {
  constructor(private logger: ILogger) {}
  
  getById(id: number): User {
    this.logger.log(`Getting user ${id}`);
    return new User(id, `User ${id}`, `user${id}@example.com`);
  }
}

class EmailService implements IEmailService {
  constructor(private logger: ILogger) {}
  
  sendEmail(to: string, subject: string, body: string): boolean {
    this.logger.log(`Sending email to ${to} with subject "${subject}"`);
    return true;
  }
}

class UserService {
  constructor(
    private repository: IUserRepository,
    private emailService: IEmailService,
    private logger: ILogger
  ) {}
  
  notifyUser(userId: number, message: string): boolean {
    this.logger.log(`Preparing to notify user ${userId}`);
    
    const user = this.repository.getById(userId);
    if (!user) return false;
    
    return this.emailService.sendEmail(
      user.email,
      "Notification",
      message
    );
  }
}

// Set up container
function setupContainer(): Container {
  const container = new Container();
  
  // Register services with appropriate lifetimes
  container.register<ILogger>("logger", () => new ConsoleLogger(), Lifetime.Singleton);
  
  container.register<IUserRepository>("userRepository", (c) => {
    return new UserRepository(c.resolve<ILogger>("logger"));
  }, Lifetime.Scoped);
  
  container.register<IEmailService>("emailService", (c) => {
    return new EmailService(c.resolve<ILogger>("logger"));
  }, Lifetime.Transient);
  
  container.register<UserService>("userService", (c) => {
    return new UserService(
      c.resolve<IUserRepository>("userRepository"),
      c.resolve<IEmailService>("emailService"),
      c.resolve<ILogger>("logger")
    );
  }, Lifetime.Transient);
  
  return container;
}

// Demo the container
function demonstrateContainer() {
  const container = setupContainer();
  
  console.log("Resolving singleton logger twice:");
  const logger1 = container.resolve<ILogger>("logger");
  const logger2 = container.resolve<ILogger>("logger");
  console.log("Same instance?", logger1 === logger2); // Should be true for singleton
  
  console.log("\nUsing scoped services:");
  container.withScope(() => {
    // These will be the same instance within this scope
    const repo1 = container.resolve<IUserRepository>("userRepository");
    const repo2 = container.resolve<IUserRepository>("userRepository");
    console.log("Same repository instance within scope?", repo1 === repo2); // Should be true
    
    // Transient services are always different
    const email1 = container.resolve<IEmailService>("emailService");
    const email2 = container.resolve<IEmailService>("emailService");
    console.log("Same email service instance?", email1 === email2); // Should be false
    
    // Use the user service
    const userService = container.resolve<UserService>("userService");
    userService.notifyUser(1, "Hello from DI container!");
  });
  
  console.log("\nUsing a different scope:");
  container.withScope(() => {
    // Will be different from the previous scope
    const repo = container.resolve<IUserRepository>("userRepository");
    console.log("Repository resolved in new scope");
  });
}

demonstrateContainer();
