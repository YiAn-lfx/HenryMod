package HenryTGZJMod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHenryPower extends TwoAmountPower {
    // 缓存映射：类名 -> (size -> AtlasRegion)
    private static final Map<String, Map<Integer, AtlasRegion>> textureCache = new HashMap<>();

    // 默认测试图像路径
    protected static final String DEFAULT_128_PATH = "HenryTGZJModResources/img/powers/Test128.png";
    protected static final String DEFAULT_48_PATH = "HenryTGZJModResources/img/powers/Test48.png";

    /**
     * 初始化图像资源（在构造函数中调用）
     */
    protected void initializeImages() {
        String className = this.getClass().getSimpleName();

        // 获取或创建该类名的缓存映射
        Map<Integer, AtlasRegion> classCache = textureCache.computeIfAbsent(className, k -> new HashMap<>());

        // 加载128x128图像
        this.region128 = classCache.computeIfAbsent(128, k -> {
            String path128 = String.format("HenryTGZJModResources/img/powers/%s128.png", className);
            return loadAtlasRegion(path128, DEFAULT_128_PATH, 128);
        });

        // 加载48x48图像
        this.region48 = classCache.computeIfAbsent(48, k -> {
            String path48 = String.format("HenryTGZJModResources/img/powers/%s48.png", className);
            return loadAtlasRegion(path48, DEFAULT_48_PATH, 48);
        });
    }

    /**
     * 加载AtlasRegion，如果主路径失败则使用备用路径
     */
    private AtlasRegion loadAtlasRegion(String primaryPath, String fallbackPath, int size) {
        try {
            // 尝试加载主路径
            return new AtlasRegion(ImageMaster.loadImage(primaryPath), 0, 0, size, size);
        } catch (Exception e) {
            // 如果主路径失败，使用备用测试图像
           // System.out.println("Failed to load texture from " + primaryPath + ", using fallback.");
            return new AtlasRegion(ImageMaster.loadImage(fallbackPath), 0, 0, size, size);
        }
    }

    /**
     * 清空缓存（可选，用于重新加载资源）
     */
    public static void clearCache() {
        textureCache.clear();
    }

    /**
     * 清理指定类名的缓存（可选）
     */
    public static void clearCacheForClass(Class<? extends AbstractHenryPower> powerClass) {
        textureCache.remove(powerClass.getSimpleName());
    }
}