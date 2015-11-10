using UnityEngine;
using System.Collections;

public class Follow : MonoBehaviour {

    public GameObject target;
    public float distance = 3f;
    public Vector3 direction = Vector3.back;

    public const float weight = 0.9f;
    private Vector3 last_postition;
    private Vector3 last_destination;

    void Start() {
        target.GetComponent<PlayerController>().movement_frame = transform;
    }

    void Update() {
        last_postition = last_postition * weight + target.transform.position * (1f - weight);
        transform.position = last_postition;

        Vector3 velocity = target.GetComponent<Rigidbody>().velocity;
        if (velocity.sqrMagnitude >= 1f) {
            velocity.Normalize();
            velocity *= distance;
            velocity.y = 0f;
            last_destination = last_destination * weight + (target.transform.position + velocity) * (1f - weight);
        }
        transform.LookAt(last_destination);

        transform.Translate(direction * distance);
	}
}
