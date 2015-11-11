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
            case "Forward":
                return Input.GetAxis(controller_name + "Forward");

            case "Strafe":
                return Input.GetAxis(controller_name + "Strafe");

            case "Jump":
                return Input.GetAxis(controller_name + "Jump");

            case "Lookleft":
                return Input.GetAxis(controller_name + "Lookleft");

            case "Lookup":
                return Input.GetAxis(controller_name + "Lookup");
        }

        Debug.Log("No input found for '" + value + "'");
        return 0f;
    }
}
