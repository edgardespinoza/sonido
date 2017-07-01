package speechtext.com.speechtotext.manager;

import java.util.Stack;

import speechtext.com.speechtotext.entry.Entry;

/**
 * Created by eespinor on 26/06/2017.
 */

public class ManagerAudioComponent {
    Stack<Entry> st = new Stack<Entry>();

    public void push(Entry a) {
        st.push(a);
    }

    public Entry pop() {
        if (!st.isEmpty())
            return st.pop();
        else
            return null;
    }
}
