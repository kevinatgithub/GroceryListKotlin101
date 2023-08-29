using GroceryList.Domain;
using GroceryList.Repositories.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace GroceryList.Repositories;

public class UserRepository : IUserRepository
{
    private readonly AppDbContext context;

    public UserRepository(AppDbContext context)
    {
        this.context = context;
    }

    public async Task<User> Create(string email, string name, int cartId, string? avatar)
    {
        var user = new User
        {
            Email = email,
            Name = name,
            CartId = cartId,
            Avatar = avatar
        };
        await context.Users.AddAsync(user);
        context.SaveChanges();
        return user;
    }

    public async Task<User?> Get(string email)
    {
        return await context.Users.FirstOrDefaultAsync(x => x.Email == email);
    }

    public async Task<ICollection<User>> GetUsers(IEnumerable<string> emails)
    {
        return await context.Users.Where(u => emails.Contains(u.Email)).ToListAsync();
    }

    public User Update(User user)
    {
        context.Users.Update(user);
        return user;
    }

    public async Task UpdateProfile(string email, string name, string avatar)
    {
        var user = await context.Users.FirstOrDefaultAsync(u => u.Email == email);
        if (user != null)
        {
            user.Name = name;
            user.Avatar = avatar;
            context.Users.Update(user);
            context.SaveChanges();
        }
    }
}
