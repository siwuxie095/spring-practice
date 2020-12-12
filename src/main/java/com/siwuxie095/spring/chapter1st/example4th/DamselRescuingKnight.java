package com.siwuxie095.spring.chapter1st.example4th;

/**
 * 营救少女的骑士
 *
 * DamselRescuingKnight 只能执行 RescueDamselQuest 探险任务
 *
 * @author Jiajing Li
 * @date 2020-12-12 11:30:35
 */
@SuppressWarnings("all")
public class DamselRescuingKnight implements Knight {

    private RescueDamselQuest quest;

    public DamselRescuingKnight() {
        this.quest = new RescueDamselQuest();
    }

    @Override
    public void embarkOnQuest() {
        quest.embark();
    }
}
