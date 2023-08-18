namespace GroceryList.Api.Models;

public class UpdateItemRequest
{
    public string Name { get; set; }
    public string Description { get; set; }
    public decimal? PricePerUnit { get; set; }
    public int Quantity { get; set; }
    public string? imgURL { get; set; }
    public string? img { get; set; }
}
