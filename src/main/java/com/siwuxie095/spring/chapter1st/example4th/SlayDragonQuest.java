package com.siwuxie095.spring.chapter1st.example4th;

import java.io.PrintStream;

/**
 * 杀掉恶龙的探险
 *
 * @author Jiajing Li
 * @date 2020-12-12 13:20:04
 */
@SuppressWarnings("all")
public class SlayDragonQuest implements Quest {

    private PrintStream stream;

    public SlayDragonQuest(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void embark() {
        stream.println("Embarking on quest to slay the dragon!");
    }

}
