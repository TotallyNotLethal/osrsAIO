package com.Anomaly.AIO.Helpers.Animations;

import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.skills.Skill;

public enum NPCAttack {
    KING_BLACK_DRAGON("King Black Dragon",
            new int[]{4638, 91, 80},
            new int[]{81, 82, 83, 84},
            null),
    DAGANNOTH_REX("Dagannoth Rex",
            new int[]{2853},
            null,
            null),

    DAGANNOTH_PRIME("Dagannoth Prime",
            null,
            new int[]{2854},
            null),

    SARACHNIS("Sarachnis",
            new int[]{8147},
            null,
            new int[]{4410}),

    DAGANNOTH_SUPREME("Dagannoth Supreme",
            null,
            null,
            new int[]{2855}
    );

    private final String name;
    private final int[] meleeAnimations;
    private final int[] magicAnimations;
    private final int[] rangedAnimations;

    NPCAttack(String name, int[] meleeAnimations, int[] magicAnimations, int[] rangedAnimations) {
        this.name = name;
        this.meleeAnimations = meleeAnimations;
        this.magicAnimations = magicAnimations;
        this.rangedAnimations = rangedAnimations;
    }

    public String getName() {
        return name;
    }

    public int[] getMeleeAnimations() {
        return meleeAnimations;
    }

    public int[] getMagicAnimations() {
        return magicAnimations;
    }

    public int[] getRangedAnimations() {
        return rangedAnimations;
    }

    public static NPCAttack findByNpcName(String npcName) {
        for (NPCAttack animation : values()) {
            if (animation.getName().equalsIgnoreCase(npcName)) {
                return animation;
            }
        }
        return null;
    }

    public Prayer getRequiredPrayer(int animation) {
        if (isInAnimationSet(animation, meleeAnimations)) {
            return Prayer.PROTECT_FROM_MELEE;
        } else if (isInAnimationSet(animation, magicAnimations)) {
            return Prayer.PROTECT_FROM_MAGIC;
        } else if (isInAnimationSet(animation, rangedAnimations)) {
            return Prayer.PROTECT_FROM_MISSILES;
        }
        return null;
    }

    private boolean isInAnimationSet(int animation, int[] animations) {
        if (animations == null) return false;
        for (int anim : animations) {
            if (anim == animation) return true;
        }
        return false;
    }

    public String getAttackType(int animation) {
        if (isInAnimationSet(animation, meleeAnimations)) {
            return "Melee";
        } else if (isInAnimationSet(animation, magicAnimations)) {
            return "Magic";
        } else if (isInAnimationSet(animation, rangedAnimations)) {
            return "Ranged";
        }
        return null;
    }
}
