using UnityEngine;
using System.Collections;

public class Movement : MonoBehaviour {

    private Rigidbody body;

	void Start () {
        body = GetComponent<Rigidbody>();
	}
	
	void FixedUpdate () {
        string controller_name = "Keyboard ";

        if (!GetComponentInParent<PlayerController>().keyboard_controlled) {
            controller_name = "Gamepad " + GetComponentInParent<PlayerController>().controller_id + " ";
        }

        Vector3 direction = new Vector3(Input.GetAxis(controller_name + "Horizontal"), Input.GetAxis("Jump"), Input.GetAxis(controller_name + "Vertical"));
        body.AddForce(direction * 15f * body.mass);
	}
}
