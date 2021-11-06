package de.teamlapen.vampirism_integrations.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.core.ModRecipes;
import de.teamlapen.vampirism.inventory.recipes.ShapedWeaponTableRecipe;
import de.teamlapen.vampirism.inventory.recipes.ShapelessWeaponTableRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Allows you to add or remove weapon table recipes.
 *
 * @docParam this <recipetype:vampirism:weapontable_crafting>
 */
@ZenRegister
@Document("mods/vampirism/WeaponTableRecipe")
@ZenCodeType.Name("mods.vampirism.WeaponTableRecipe")
public class CTWeaponTableRecipeManager implements IRecipeManager {

    @Override
    public IRecipeType getRecipeType() {
        return ModRecipes.WEAPONTABLE_CRAFTING_TYPE;
    }

    /**
     * Adds a shapeless recipe to the weapon table
     *
     * @param recipePath The recipe name, without the resource mod id
     * @param result The recipes result
     * @param ingredients The input items for the crafting grid
     * @param level The required hunter level to use this recipe
     * @param lava The amount of lava consumed by this recipe
     * @param skills The skills the player must have unlocked to use this recipe
     * @docParam recipePath "iron_sword"
     * @docParam result <item:minecraft:iron_sword>
     * @docParam ingredients [<item:minecraft:iron_ingot>,<item:minecraft:iron_ingot>,<item:minecraft:stick>]
     * @docParam level 8
     * @docParam lava 5
     * @docParam skills [<skill:vampirism:tech_weapons>]
     */
    @ZenCodeType.Method
    public void addShapeless(String recipePath, IItemStack result, IIngredient[] ingredients, int level, int lava, ISkill[] skills) {
        ResourceLocation id = new ResourceLocation("crafttweaker", recipePath);
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.addAll(Arrays.stream(ingredients).map(IIngredient::asVanillaIngredient).collect(Collectors.toList()));
        ShapelessWeaponTableRecipe recipe = new ShapelessWeaponTableRecipe(id, "", nonnulllist, result.getInternal(), level, lava, skills);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, "shapeless"));
    }

    /**
     * Adds a shaped recipe to the weapon table
     *
     * @param recipePath The recipe name, without the resource mod id
     * @param result The recipes result
     * @param ingredients The input items for the crafting grid
     * @param level The required hunter level to use this recipe
     * @param lava The amount of lava consumed by this recipe
     * @param skills The skills the player must have unlocked to use this recipe
     * @docParam recipePath "iron_sword"
     * @docParam result <item:minecraft:iron_sword>
     * @docParam ingredients [[air,<item:minecraft:iron_ingot>, air],[air,<item:minecraft:iron_ingot>, air],[air,<item:minecraft:stick>, air]]
     * @docParam level 8
     * @docParam lava 5
     * @docParam skills [<skill:vampirism:tech_weapons>]
     */
    @ZenCodeType.Method
    public void addShaped(String recipePath, IItemStack result, IIngredient[][] ingredients, int level, int lava, ISkill[] skills) {
        ResourceLocation id = new ResourceLocation("crafttweaker", recipePath);
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.addAll(Arrays.stream(ingredients).flatMap(Arrays::stream).map(IIngredient::asVanillaIngredient).collect(Collectors.toList()));
        ShapedWeaponTableRecipe recipe = new ShapedWeaponTableRecipe(id, "", ingredients[0].length, ingredients.length, nonnulllist, result.getInternal(), level, skills, lava);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, "shaped"));
    }
}
