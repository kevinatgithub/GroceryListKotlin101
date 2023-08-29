using System.ComponentModel.DataAnnotations;

namespace GroceryList.Api.Models;

public class SignUpRequest
{
    [Required]
    [EmailAddress]
    public string Email { get; set; }
    [Required]
    public string Name { get; set; }
    [Required]
    public string Password { get; set; }
    [Required]
    public string? Avatar { get; set; }
}
