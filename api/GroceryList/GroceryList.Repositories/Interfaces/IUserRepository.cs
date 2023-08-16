using GroceryList.Domain;

namespace GroceryList.Repositories.Interfaces;

public interface IUserRepository
{
    public Task<User> Create(string email, string name, int cartId);
    public User Update(User user);
    public Task<User?> Get(string email);
}
