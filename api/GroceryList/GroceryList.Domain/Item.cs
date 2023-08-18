namespace GroceryList.Domain;

public class Item
{
    public int Id { get; set; }
    public int CartId { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public decimal? PricePerUnit { get; set; }
    public decimal? TotalPrice
    {
        get
        {
            return PricePerUnit * Quantity;
        }
        private set
        {

        }
    }
    public int Quantity { get; set; }
    public string? imgUrl { get; set; }
    public string? img { get; set; }
    public int? AlternativeItemId { get; set; }
    public bool IsPrimary { get; set; }
    public int Status { get; set; }
}
