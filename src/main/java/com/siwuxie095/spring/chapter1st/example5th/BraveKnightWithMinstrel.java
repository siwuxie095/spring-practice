package com.siwuxie095.spring.chapter1st.example5th;

import com.siwuxie095.spring.chapter1st.example4th.Knight;
import com.siwuxie095.spring.chapter1st.example4th.Quest;

/**
 * BraveKnightWithMinstrel 必须要调用 Minstrel 的方法
 *
 * @author Jiajing Li
 * @date 2020-12-16 07:53:49
 */
@SuppressWarnings("all")
public class BraveKnightWithMinstrel implements Knight {

    private Quest quest;

    private Minstrel minstrel;

    public BraveKnightWithMinstrel(Quest quest, Minstrel minstrel) {
        this.quest = quest;
        this.minstrel = minstrel;
    }

    /**
     * Knight 应该管理它的 Minstrel 吗？
     */
    @Override
    public void embarkOnQuest() {
        minstrel.singBeforeQuest();
        quest.embark();
        minstrel.singAfterQuest();
    }
}
