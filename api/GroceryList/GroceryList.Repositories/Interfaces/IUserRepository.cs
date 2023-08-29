using GroceryList.Domain;

namespace GroceryList.Repositories.Interfaces;

public interface IUserRepository
{
    public Task<User> Create(string email, string name, int cartId, string? avatar);
    public User Update(User user);
    public Task<User?> Get(string email);
    public Task<ICollection<User>> GetUsers(IEnumerable<string> emails);
    public Task UpdateProfile(string email, string name, string avatar);
}
