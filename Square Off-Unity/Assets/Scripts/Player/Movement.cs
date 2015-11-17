using UnityEngine;
using System.Collections;

public class Movement : MonoBehaviour {

    private Rigidbody body;
    private PlayerController controller;

	void Start () {
        body = GetComponent<Rigidbody>();
        controller = GetComponentInParent<PlayerController>();
    }
	
	void FixedUpdate () {

        Vector3 direction = new Vector3(0, controller.getInput("Jump"), 0);
        Transform cam = controller.movement_frame;
        direction += controller.getInput("Forward") * cam.transform.forward + controller.getInput("Strafe") * cam.transform.right;

        body.AddForce(direction * 15f * body.mass);
	}

}
