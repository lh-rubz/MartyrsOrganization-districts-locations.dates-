package application;

import java.util.ArrayList;

public class Stack {
    private ArrayList<Object> array;
    private int top;

    public Stack() {
        array = new ArrayList<>();
        top = -1;
    }

    public int getSize() {
        return top + 1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public void push(Object item) {
        array.add(item);
        top++;
    }

    public Object pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return null;
        }
        Object item = array.remove(top);
        top--;
        return item;
    }

    public Object peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return null;
        }
        return array.get(top);
    }

   
}
