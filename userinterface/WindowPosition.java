// tabs=4
//************************************************************
//	COPYRIGHT Sandeep Mitra, 2015 - ALL RIGHTS RESERVED
//
//
//*************************************************************

// Specify the package
package userinterface;

// system imports
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.stage.Screen;

// project imports

/**
 * Provides a means of placing the main stage of the application
 * in the center of the screen, top left corner, bottom right
 * corner, top right corner, bottom left corner
 *
 */
//==============================================================
public class WindowPosition
{
    private static Rectangle2D primaryScreenBounds =
            Screen.getPrimary().getVisualBounds();



    /**
     * Used to place the stage in the center of the screen
     *
     * @param	s	Stage to place at the center of the screen
     *
     */
    //--------------------------------------------------------------------------
    public static void placeCenter(Stage s)
    {
        if (s != null)
        {
            s.centerOnScreen();
        }
    }

    /**
     * Used to place a stage at the top left corner of te screen
     *
     * @param	s	Stage to place at the top left corner of the screen
     *
     */
    //--------------------------------------------------------------------------
    public static void placeTopLeft(Stage s)
    {
        if (s != null)
        {
            s.setX(primaryScreenBounds.getMinX());
            s.setY(primaryScreenBounds.getMinY());
        }
    }

    /**
     * Used to place a stage at the top right corner of the screen
     *
     * @param	s	Stage to place at the top right corner of the screen
     *
     */
    //--------------------------------------------------------------------------
    public static void placeTopRight(Stage s)
    {
        if (s != null)
        {
            s.setX(primaryScreenBounds.getMinX() +
                    primaryScreenBounds.getWidth() - s.getWidth());
            s.setY(primaryScreenBounds.getMinY());
        }
    }

    /**
     * Used to place a stage at the bottom left corner of te screen
     *
     * @param	s	Stage to place at the bottom left corner of the screen
     *
     */
    //--------------------------------------------------------------------------
    public static void placeBottomLeft(Stage s)
    {
        if (s != null)
        {
            s.setX(primaryScreenBounds.getMinX());
            s.setY(primaryScreenBounds.getMinY() +
                    primaryScreenBounds.getHeight() - s.getHeight());
        }

    }

    /**
     * Used to place a stage at the bottom right corner of the screen
     *
     * @param	s	Stage to place at the bottom right corner of the screen
     *
     */
    //--------------------------------------------------------------------------
    public static void placeBottomRight(Stage s)
    {
        if (s != null)
        {
            s.setX(primaryScreenBounds.getMinX() +
                    primaryScreenBounds.getWidth() - s.getWidth());
            s.setY(primaryScreenBounds.getMinY() +
                    primaryScreenBounds.getHeight() - s.getHeight());
        }
    }
}


//---------------------------------------------------------------
//	Revision History:


