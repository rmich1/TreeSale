// tabs=4
//************************************************************
//	COPYRIGHT 2015 Sandeep Mitra, T M Rao and students, The
//    College at Brockport, State University of New York. -
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//************************************************************
//
// specify the package
package userinterface;

// system imports
import javafx.stage.Stage;

// project imports

/**
 * The main stage for the Brockport GUI applications.
 * All scenes are inside this one stage only.
 */
//==============================================================
public class MainStageContainer
{
    // data members

    private static Stage myInstance = null;

    // class constructor
    //----------------------------------------------------------
    private MainStageContainer ()
    {
    }

    //----------------------------------------------------------
    public static Stage getInstance()
    {
        return myInstance;
    }

    //-----------------------------------------------------------
    public static void setStage(Stage st, String title)
    {
        myInstance = st;
        myInstance.setTitle(title);
        myInstance.setResizable(false);
    }

}




