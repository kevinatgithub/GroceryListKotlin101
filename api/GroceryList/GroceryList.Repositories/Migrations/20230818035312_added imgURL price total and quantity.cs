using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace GroceryList.Repositories.Migrations
{
    public partial class addedimgURLpricetotalandquantity : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Price",
                table: "Items",
                newName: "TotalPrice");

            migrationBuilder.AddColumn<decimal>(
                name: "PricePerUnit",
                table: "Items",
                type: "decimal(18,2)",
                nullable: true);

            migrationBuilder.AddColumn<int>(
                name: "Quantity",
                table: "Items",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.AddColumn<string>(
                name: "imgUrl",
                table: "Items",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "PricePerUnit",
                table: "Items");

            migrationBuilder.DropColumn(
                name: "Quantity",
                table: "Items");

            migrationBuilder.DropColumn(
                name: "imgUrl",
                table: "Items");

            migrationBuilder.RenameColumn(
                name: "TotalPrice",
                table: "Items",
                newName: "Price");
        }
    }
}
