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

    public async Task<User> Create(string email, string name, int cartId)
    {
        var user = new User
        {
            Email = email,
            Name = name,
            CartId = cartId
        };
        await context.Users.AddAsync(user);
        context.SaveChanges();
        return user;
    }

    public async Task<User?> Get(string email)
    {
        return await context.Users.FirstOrDefaultAsync(x => x.Email == email);
    }

    public User Update(User user)
    {
        context.Users.Update(user);
        return user;
    }
}
