package com.siwuxie095.spring.chapter1st.example4th;

/**
 * 营救少女的探险
 *
 * @author Jiajing Li
 * @date 2020-12-12 11:30:52
 */
@SuppressWarnings("all")
public class RescueDamselQuest implements Quest {

    @Override
    public void embark() {
        System.out.println("Embarking on a quest to rescue the damsel.");
    }
}
