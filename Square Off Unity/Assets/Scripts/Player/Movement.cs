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

        Vector3 direction = new Vector3(controller.getInput("Horizontal"), controller.getInput("Jump"), controller.getInput("Vertical"));
        body.AddForce(direction * 15f * body.mass);
	}
}
