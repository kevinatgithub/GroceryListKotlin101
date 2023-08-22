namespace GroceryList.Api.Models;

public class UpdateUserRequest
{
    public string Name { get; set; }
    public string? OldPassword { get; set; }
    public string? NewPassword { get; set; }
    public string? Avatar { get; set; }
}
