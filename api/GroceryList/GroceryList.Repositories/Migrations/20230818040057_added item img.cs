using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace GroceryList.Repositories.Migrations
{
    public partial class addeditemimg : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "img",
                table: "Items",
                type: "nvarchar(max)",
                nullable: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "img",
                table: "Items");
        }
    }
}
