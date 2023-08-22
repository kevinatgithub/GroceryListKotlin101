using GroceryList.Domain;

namespace GroceryList.Services.Interfaces;

public interface IUserService
{
    public Task<User> Create(string email, string name, int cartId);
    public User Update(User user);
    public Task<User?> Get(string email);
    public Task UpdateProfileAsync(string email, string name, string avatar);
}
