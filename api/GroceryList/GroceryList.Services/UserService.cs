using GroceryList.Domain;
using GroceryList.Repositories.Interfaces;
using GroceryList.Services.Interfaces;

namespace GroceryList.Services;

public class UserService : IUserService
{
    private readonly IUserRepository repository;

    public UserService(IUserRepository repository)
    {
        this.repository = repository;
    }

    public async Task<User> Create(string email, string name, int cartId, string? avatar)
    {
        return await repository.Create(email, name, cartId, avatar);
    }

    public async Task<User?> Get(string email)
    {
        return await repository.Get(email);
    }

    public User Update(User user)
    {
        return repository.Update(user);
    }

    public async Task UpdateProfileAsync(string email, string name, string avatar)
    {
        await repository.UpdateProfile(email, name, avatar);
    }
}
