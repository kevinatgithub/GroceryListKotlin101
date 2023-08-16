using System.ComponentModel.DataAnnotations;

namespace GroceryList.Domain;

public class User
{
    [Key]
    public string Email { get; set; }
    public string Name { get; set; }
    public int CartId { get; set; }
    public string? Avatar { get; set; }
}
