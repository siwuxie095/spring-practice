package com.siwuxie095.spring.chapter1st.example4th;

/**
 * 勇敢的骑士
 *
 * BraveKnight 足够灵活可以接受任何赋予他的探险任务
 *
 * @author Jiajing Li
 * @date 2020-12-12 13:16:02
 */
@SuppressWarnings("all")
public class BraveKnight implements Knight {

    private Quest quest;

    /**
     * Quest 被注入进来
     */
    public BraveKnight(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void embarkOnQuest() {
        quest.embark();
    }
}
