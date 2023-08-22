using GroceryList.Domain;

namespace GroceryList.Services.Interfaces;

public interface IUserCartService
{
    public Task<ICollection<User>> GetCartUsersAync(int cartId);
    public Task<UserCart> GetUserCartAsync(string email);
}
