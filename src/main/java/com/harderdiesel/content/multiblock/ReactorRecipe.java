package com.harderdiesel.content.multiblock;

import com.simibubi.create.content.processing.recipe.HeatCondition;

/**
 * Рецепт, исполняемый мультиблочным реактором ({@link ReactorBlockEntity}):
 * крекинг-реактор и сепаратор используют один протокол применения —
 * проверка ингредиентов в мульти-танке контроллера и залив результатов
 * в блоки над ним.
 */
public interface ReactorRecipe {

    /**
     * Проверяет, что мульти-танк реактора содержит все жидкие ингредиенты,
     * а блоки вывода над контроллером имеют место. При {@code simulate == false}
     * также сливает ингредиенты и заливает результаты.
     */
    boolean apply(ReactorBlockEntity be, boolean simulate);

    HeatCondition getRequiredHeat();

    int getProcessingDuration();
}
