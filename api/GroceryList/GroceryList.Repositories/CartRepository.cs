using GroceryList.Domain;
using GroceryList.Repositories.Interfaces;

namespace GroceryList.Repositories;

public class CartRepository : ICartRepository
{
    private readonly AppDbContext _context;

    public CartRepository(AppDbContext context)
    {
        _context = context ?? throw new ArgumentNullException(nameof(context));
    }

    public async Task<Cart> CreateCartAsync(string createdBy)
    {
        Cart cart = new Cart
        {
            CreatedBy = createdBy,
        };

        await _context.Carts.AddAsync(cart);
        _context.SaveChanges();
        return cart;
    }
}
