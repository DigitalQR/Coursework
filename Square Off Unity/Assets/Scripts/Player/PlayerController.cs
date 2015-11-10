using UnityEngine;
using System.Collections;

public class PlayerController : MonoBehaviour {

    public Color colour = Color.white;
    public uint controller_id;
    public bool keyboard_controlled = false;
    public Transform movement_frame;

    void Start() {
        if (movement_frame == null) movement_frame = transform;
    }

    //Return's the appropriate input for the player
    public float getInput(string value)
    {
        string controller_name = "Keyboard ";
        if (!keyboard_controlled)
        {
            controller_name = "Gamepad " + controller_id + " ";
        }

        switch (value)
        {
            case "Horizontal":
                return Input.GetAxis(controller_name + "Horizontal");

            case "Vertical":
                return Input.GetAxis(controller_name + "Vertical");

            case "Jump":
                return Input.GetAxis(controller_name + "Jump");
        }

        Debug.Log("No input found for '" + value + "'");
        return 0f;
    }
}
