package HenryTGZJMod.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static HenryTGZJMod.characters.Henry.PlayerColorEnum.HENRY_CLASS;

public abstract class AbstractHenryPotion extends CustomPotion {


    public AbstractHenryPotion(String name, String id, AbstractPotion.PotionRarity rarity, AbstractPotion.PotionSize size, AbstractPotion.PotionColor color) {
        super(name, id, rarity, size, color);
    }

}
