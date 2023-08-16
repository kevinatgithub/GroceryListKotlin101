using GroceryList.Domain;

namespace GroceryList.Repositories.Interfaces;

public interface ICartRepository
{
    public Task<Cart> CreateCartAsync(string createdBy);
}
