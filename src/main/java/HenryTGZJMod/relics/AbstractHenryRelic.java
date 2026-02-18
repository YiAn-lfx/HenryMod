package HenryTGZJMod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class AbstractHenryRelic extends CustomRelic {


//    public AbstractHenryRelic(String id, boolean needOutline, RelicTier tier, LandingSound sfx) {
//        super(id, getTexture(id, false), getTexture(id, true), tier, sfx);
//    }

    public AbstractHenryRelic(String id, RelicTier tier, LandingSound sfx) {
        super(id, getTexture(id, false), getTexture(id, true), tier, sfx);
    }
    public AbstractHenryRelic(String id, Texture texture, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(id, "", tier, sfx);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.setTexture(texture);
    }



    private static Texture getTexture(String id, boolean outline) {
        // 去掉mod前缀获取文件名
        String name = id.replace("HenryTGZJMod:", "");
        String path = String.format("HenryTGZJModResources/img/relics/%s%s.png",
                name, outline ? "_outline" : "");

        Texture tex = ImageMaster.loadImage(path);

        // 如果找不到，用测试图片
        if (tex == null) {
            String testPath = String.format("HenryTGZJModResources/img/relics/test%s.png",
                    outline ? "_outline" : "");
            tex = ImageMaster.loadImage(testPath);
        }

        return tex;
    }


}
