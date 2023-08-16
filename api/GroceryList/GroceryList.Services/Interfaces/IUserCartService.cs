using GroceryList.Domain;

namespace GroceryList.Services.Interfaces;

public interface IUserCartService
{
    public Task<ICollection<UserCart>> GetCartUsers(int cartId);
}
