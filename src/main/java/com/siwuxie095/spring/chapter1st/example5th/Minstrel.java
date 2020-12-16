package com.siwuxie095.spring.chapter1st.example5th;

import java.io.PrintStream;

/**
 * 吟游诗人
 *
 * 吟游诗人是中世纪的音乐记录器
 *
 * @author Jiajing Li
 * @date 2020-12-15 08:35:53
 */
@SuppressWarnings("all")
public class Minstrel {

    private PrintStream stream;

    public Minstrel(PrintStream stream) {
        this.stream = stream;
    }

    /**
     * 探险之前调用
     */
    public void singBeforeQuest() {
        stream.println("Fa la la, the knight is so brave!");
    }

    /**
     * 探险之后调用
     */
    public void singAfterQuest() {
        stream.println("Tee hee hee, the brave knight did embark on a quest!");
    }

}
