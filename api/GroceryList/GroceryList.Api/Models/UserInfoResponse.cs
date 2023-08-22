using GroceryList.Domain;

namespace GroceryList.Api.Models;

public class UserInfoResponse
{
    public string Email { get; set; }
    public string Name { get; set; }
    public int CartId { get; set; }
    public ICollection<Item> CartItems { get; set; }
    public string? Avatar { get; set; }
}
