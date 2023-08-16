using GroceryList.Domain;
using GroceryList.Repositories.Interfaces;
using Microsoft.EntityFrameworkCore;
using System.Reflection.Metadata.Ecma335;

namespace GroceryList.Repositories;

public class UserCartRepository : IUserCartRepository
{
    private readonly AppDbContext _context;

    public UserCartRepository(AppDbContext context)
    {
        _context = context ?? throw new ArgumentNullException(nameof(context));
    }

    public async Task<UserCart> CreateUserCartAsync(int cartId, string email, string createdBy)
    {
        UserCart userCart = new UserCart
        {
            CartId = cartId,
            CreatedBy = createdBy,
            Email = email,
        };

        await _context.UserCarts.AddAsync(userCart);
        _context.SaveChanges();
        return userCart;
    }

    public async Task<ICollection<UserCart>> GetAllUsersFromCartAsync(int cartId)
    {
        return await _context.UserCarts.Where(uc => uc.CartId == cartId).ToListAsync();
    }

    public async Task<UserCart?> GetUserCartByIdAndEmailAsync(int cartId, string email)
    {
        return await _context.UserCarts.FirstOrDefaultAsync(uc => uc.CartId == cartId && uc.Email == email);
    }

    public async Task RemoveUserCartAsync(int userCartId)
    {
        UserCart? userCart = await _context.UserCarts.FirstOrDefaultAsync(uc => uc.CartId == userCartId);
        if (userCart != null)
        {
            _context.UserCarts.Remove(userCart);
            _context.SaveChanges();
        }
    }
}
