package HenryTGZJMod.modcore;

import HenryTGZJMod.DynamicVariable.SecondBlockVariable;
import HenryTGZJMod.DynamicVariable.SecondDamageVariable;
import HenryTGZJMod.DynamicVariable.StanceCostVariable;
import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.characters.Henry;
import HenryTGZJMod.potions.AbstractHenryPotion;
import HenryTGZJMod.relics.AbstractHenryRelic;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.nio.charset.StandardCharsets;

import static HenryTGZJMod.characters.Henry.PlayerColorEnum.HENRY_CLASS;
import static HenryTGZJMod.characters.Henry.PlayerColorEnum.H_BROWN;
import static com.megacrit.cardcrawl.core.Settings.language;

@SpireInitializer
public class HenryTGZJMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber,  PostInitializeSubscriber { // 实现接口
    // 人物选择界面按钮的图片
    private static final String HENRY_CLASS_BUTTON = "HenryTGZJModResources/img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String HENRY_CLASS_PORTRAIT = "HenryTGZJModResources/img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "HenryTGZJModResources/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "HenryTGZJModResources/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "HenryTGZJModResources/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "HenryTGZJModResources/img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "HenryTGZJModResources/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "HenryTGZJModResources/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "HenryTGZJModResources/img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "HenryTGZJModResources/img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENERGY_ORB = "HenryTGZJModResources/img/char/cost_orb.png";

    public static final Color MY_COLOR = new Color(168.0F / 255.0F, 139.0F / 255.0F, 65.0F / 255.0F, 1.0F);

    public HenryTGZJMod() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
        // 这里注册颜色
        BaseMod.addColor(H_BROWN, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512,BG_SKILL_512,BG_POWER_512, ENERGY_ORB,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,BIG_ORB,SMALL_ORB);
    }

    public static void initialize() {
        new HenryTGZJMod();
    }

    public void receivePostInitialize() {
        registerPotions();
    }


    //向basemod注册卡牌
    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new StanceCostVariable());
        BaseMod.addDynamicVariable(new SecondDamageVariable());
        BaseMod.addDynamicVariable(new SecondBlockVariable());
        new AutoAdd("HenryTGZJMod")
                .packageFilter(AbstractHenryCard.class)
                .setDefaultSeen(true)
                .cards();

    }

    public static void registerPotions() {
        new AutoAdd("HenryTGZJMod")
                .packageFilter(AbstractHenryPotion.class)
                .any(AbstractHenryPotion.class, (info, potion) -> {
                    BaseMod.addPotion(potion.getClass(), null, null, null, potion.ID, HENRY_CLASS);
                });
    }

    // 向basemod注册人物
    @Override
    public void receiveEditCharacters() {

        BaseMod.addCharacter(new Henry(CardCrawlGame.playerName), HENRY_CLASS_BUTTON, HENRY_CLASS_PORTRAIT, HENRY_CLASS);
    }
    // 向basemod注册遗物
    @Override
    public void receiveEditRelics() {
        new AutoAdd("HenryTGZJMod")
                .packageFilter(AbstractHenryRelic.class)
                .any(AbstractHenryRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, H_BROWN);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    // 向basemod注册关键词
    @Override
    public void receiveEditKeywords(){
        Gson gson = new Gson();
        String lang = "ZHS";
        if (language == GameLanguage.ENG){
            lang = "ENG";
        }
        String json = Gdx.files.internal("HenryTGZJModResources/localization/" + lang + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords){
                BaseMod.addKeyword("henrytgzjmod", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }



    public void receiveEditStrings(){
        String lang;
        if (language == GameLanguage.ENG){
            lang = "ENG";
        }else {
            lang = "ZHS";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class,"HenryTGZJModResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "HenryTGZJModResources/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "HenryTGZJModResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "HenryTGZJModResources/localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "HenryTGZJModResources/localization/" + lang + "/ui.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, "HenryTGZJModResources/localization/" + lang + "/potions.json");
    }
}