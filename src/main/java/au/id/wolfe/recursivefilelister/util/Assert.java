package au.id.wolfe.recursivefilelister.util;

public final class Assert
{
  public static void argumentIsNotNull(String message, Object parameter)
  {
    argumentIsTrue(message, parameter != null);
  }

  public static void stateIsNotNull(String message, Object state)
  {
    stateIsTrue(message, state != null);
  }

  public static void stateIsTrue(String message, Boolean state)
  {
    if (state){
        return;
    }
    throw new IllegalStateException(message);
  }

  public static void argumentIsTrue(String message, Boolean state)
  {
    if (state){
        return;
    }
    throw new IllegalArgumentException(message);
  }
}