package me.ci.sarica.games;

public class Joystick
{
    // The main control stick
    // Usually used for movement
    private float joystick1X;
    private float joystick1Y;

    // The secondary control stick
    // Usually used for camera control
    private float joystick2X;
    private float joystick2Y;

    // Buttons held
    private boolean aButton;
    private boolean bButton;
    private boolean xButton;
    private boolean yButton;
    private boolean startButton;
    private boolean selectButton;
    private boolean leftButton;
    private boolean rightButton;
    private boolean l2Button;
    private boolean r2Button;
    private boolean dUpButton;
    private boolean dRightButton;
    private boolean dDownButton;
    private boolean dLeftButton;

    // Buttons previous states
    private boolean aButtonLast;
    private boolean bButtonLast;
    private boolean xButtonLast;
    private boolean yButtonLast;
    private boolean startButtonLast;
    private boolean selectButtonLast;
    private boolean leftButtonLast;
    private boolean rightButtonLast;
    private boolean l2ButtonLast;
    private boolean r2ButtonLast;
    private boolean dUpButtonLast;
    private boolean dRightButtonLast;
    private boolean dDownButtonLast;
    private boolean dLeftButtonLast;

    public void updateControls(float[] inputs)
    {
        // Update previous states
        aButtonLast = aButton;
        bButtonLast = bButton;
        xButtonLast = xButton;
        yButtonLast = yButton;
        startButtonLast = startButton;
        selectButtonLast = selectButton;
        leftButtonLast = leftButton;
        rightButtonLast = rightButton;
        l2ButtonLast = l2Button;
        r2ButtonLast = r2Button;
        dUpButtonLast = dUpButton;
        dRightButtonLast = dRightButton;
        dDownButtonLast = dDownButton;
        dLeftButtonLast = dLeftButton;

        // Update joystick inputs
        joystick1X = inputs[0] * 2f - 1f;
        joystick1Y = inputs[1] * 2f - 1f;
        joystick2X = inputs[2] * 2f - 1f;
        joystick2Y = inputs[3] * 2f - 1f;

        // Update held buttons
        aButton = inputs[4] >= 0.5f;
        bButton = inputs[5] >= 0.5f;
        xButton = inputs[6] >= 0.5f;
        yButton = inputs[7] >= 0.5f;
        startButton = inputs[8] >= 0.5f;
        selectButton = inputs[9] >= 0.5f;
        leftButton = inputs[10] >= 0.5f;
        rightButton = inputs[11] >= 0.5f;
        l2Button = inputs[12] >= 0.5f;
        r2Button = inputs[13] >= 0.5f;
        dUpButton = inputs[14] >= 0.5f;
        dRightButton = inputs[15] >= 0.5f;
        dDownButton = inputs[16] >= 0.5f;
        dLeftButton = inputs[17] >= 0.5f;
    }

    public float getJoystick1X()
    {
        return joystick1X;
    }

    public float getJoystick1Y()
    {
        return joystick1Y;
    }

    public float getJoystick2X()
    {
        return joystick2X;
    }

    public float getJoystick2Y()
    {
        return joystick2Y;
    }

    public boolean holdingAButton()
    {
        return aButton;
    }

    public boolean holdingBButton()
    {
        return bButton;
    }

    public boolean holdingXButton()
    {
        return xButton;
    }

    public boolean holdingYButton()
    {
        return yButton;
    }

    public boolean holdingStartButton()
    {
        return startButton;
    }

    public boolean holdingSelectButton()
    {
        return selectButton;
    }

    public boolean holdingLeftButton()
    {
        return leftButton;
    }

    public boolean holdingRightButton()
    {
        return rightButton;
    }

    public boolean holdingLeft2Button()
    {
        return l2Button;
    }

    public boolean holdingRight2Button()
    {
        return r2Button;
    }

    public boolean holdingDUpButton()
    {
        return dUpButton;
    }

    public boolean holdingDRightButton()
    {
        return dRightButton;
    }

    public boolean holdingDDownButton()
    {
        return dDownButton;
    }

    public boolean holdingDLeftButton()
    {
        return dLeftButton;
    }

    public boolean pressedAButton()
    {
        return aButtonLast;
    }

    public boolean pressedBButton()
    {
        return bButtonLast;
    }

    public boolean pressedXButton()
    {
        return xButtonLast;
    }

    public boolean pressedYButton()
    {
        return yButtonLast;
    }

    public boolean pressedStartButton()
    {
        return startButtonLast;
    }

    public boolean pressedSelectButton()
    {
        return selectButtonLast;
    }

    public boolean pressedLeftButton()
    {
        return leftButtonLast;
    }

    public boolean pressedRightButton()
    {
        return rightButtonLast;
    }

    public boolean pressedLeft2Button()
    {
        return l2ButtonLast;
    }

    public boolean pressedRight2Button()
    {
        return r2ButtonLast;
    }

    public boolean pressedDUpButton()
    {
        return dUpButtonLast;
    }

    public boolean pressedDRightButton()
    {
        return dRightButtonLast;
    }

    public boolean pressedDDownButton()
    {
        return dDownButtonLast;
    }

    public boolean pressedDLeftButton()
    {
        return dLeftButtonLast;
    }

    public int getInputSize()
    {
        return 18;
    }
}
