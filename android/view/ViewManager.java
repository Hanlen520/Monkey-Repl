
package android.view;

public interface ViewManager {
    public void addView(View view, ViewGroup.LayoutParams params);

    public void updateViewLayout(View view, ViewGroup.LayoutParams params);

    public void removeView(View view);
}
