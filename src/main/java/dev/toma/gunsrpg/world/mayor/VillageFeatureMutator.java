package dev.toma.gunsrpg.world.mayor;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class VillageFeatureMutator {

    private static final Map<String, List<VillageVariantEntry>> VARIANTS = Util.make(new HashMap<>(), map -> {
        registerSingleVariant(map, "desert", 45);
        registerSingleVariant(map, "savanna", 100);
        registerSingleVariant(map, "snowy", 100);
        registerSingleVariant(map, "plains", 100);
        registerSingleVariant(map, "taiga", 100);
    });

    public static void mutateVanillaVillages() {
        for (List<VillageVariantEntry> list : VARIANTS.values()) {
            addMayorStructures(list);
        }
    }

    public static void addMayorStructures(List<VillageVariantEntry> list) {
        for (VillageVariantEntry entry : list) {
            addMayorStructure(entry);
        }
    }

    private static void addMayorStructure(VillageVariantEntry entry) {
        ResourceLocation key = new ResourceLocation(entry.getTargetVariant());
        JigsawPattern pattern = WorldGenRegistries.TEMPLATE_POOL.get(key);
        pattern.templates.clear();
        Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer> pair = Pair.of(JigsawPiece.legacy(entry.getResourcePath()), entry.getCount());
        JigsawPiece piece = pair.getFirst().apply(JigsawPattern.PlacementBehaviour.RIGID);
        pattern.rawTemplates = ImmutableList.<Pair<JigsawPiece, Integer>>builder()
                .addAll(pattern.rawTemplates)
                .add(Pair.of(piece, pair.getSecond()))
                .build();
        pattern.rawTemplates.forEach(rawTemplate -> {
            JigsawPiece jigsawPiece = rawTemplate.getFirst();
            for (int i = 0; i < rawTemplate.getSecond(); i++) {
                pattern.templates.add(jigsawPiece);
            }
        });
    }

    private static void registerSingleVariant(Map<String, List<VillageVariantEntry>> map, String variant, int count) {
        String templatePool = "village/" + variant + "/houses";
        VillageVariantEntry entry = new VillageVariantEntry(templatePool, templatePool + "/mayor_home", count);
        List<VillageVariantEntry> list = ImmutableList.of(entry);
        map.put(variant, list);
    }
}
