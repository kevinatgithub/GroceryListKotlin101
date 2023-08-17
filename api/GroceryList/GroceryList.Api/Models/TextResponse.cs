namespace GroceryList.Api.Models;

public class TextResponse
{
    public TextResponse(string text)
    {
        Text = text;
    }

    public string Text { get; set; }
}
