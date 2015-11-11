using UnityEngine;
using System.Collections;

public class Follow : MonoBehaviour {

    private static uint camera_track = 0;

    public GameObject target;
    public float distance = 3f;
    public Vector3 direction = Vector3.back;

    public const float weight = 0.9f;
    private Vector3 last_postition;
    private Vector3 last_destination;

    void Start() {
        target.GetComponent<PlayerController>().movement_frame = transform;
        setupViewPort();
    }

    private void setupViewPort() {
        Camera cam = GetComponent<Camera>();

        switch (camera_track)
        {
            case 0:
                cam.rect = new Rect(0.0f, 0.5f, 0.5f, 0.5f);
                break;
            case 1:
                cam.rect = new Rect(0.5f, 0.5f, 0.5f, 0.5f);
                break;
            case 2:
                cam.rect = new Rect(0.0f, 0.0f, 0.5f, 0.5f);
                break;
            case 3:
                cam.rect = new Rect(0.5f, 0.0f, 0.5f, 0.5f);
                break;
            default:
                cam.rect = new Rect(0.4f, 0.4f, 0.2f, 0.2f);
                break;
        }

        camera_track++;
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
