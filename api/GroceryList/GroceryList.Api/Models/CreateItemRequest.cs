namespace GroceryList.Api.Models;

public class CreateItemRequest
{
    public string Name { get; set; }
    public string Description { get; set; }
    public decimal? PricePerUnit { get; set; }
    public int Quantity { get; set; }
    public int? AlternativeItemId { get; set; }
    public string? imgUrl { get; set; }
    public string? img { get; set; }
}
