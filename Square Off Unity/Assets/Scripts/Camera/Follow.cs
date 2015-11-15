using UnityEngine;
using System.Collections;

public class Follow : MonoBehaviour {

    private static uint camera_track = 0;

    public GameObject target;
    public float distance = 3f;
    public Vector3 direction = Vector3.back;
    [Range(0f,1f)]
    private float angular_acceleration = 0.7f;

    private const float weight = 0.9f;
    private Vector3 last_postition;
    private Vector3 last_destination;

    private float angle;
    private float last_input;

    void Start() {
        target.GetComponent<PlayerController>().movement_frame = transform;
        setupViewPort();

        last_postition = target.transform.position;
        transform.Translate(direction * distance);


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

        float input = target.GetComponent<PlayerController>().getInput("Lookleft");
        if (input != 0 && last_input != input) {
            input += last_input * angular_acceleration;
        }
        last_input = input;

        angle += input;
        Vector3 point_at = Quaternion.AngleAxis(angle, Vector3.up) * Vector3.forward;
        last_destination = last_postition + point_at;
        
        transform.LookAt(last_destination);

        transform.Translate(direction * distance);


        RaycastHit hit;

        Vector3 to_camera = transform.position - last_destination;

        Debug.DrawRay(last_destination + to_camera.normalized, to_camera - to_camera.normalized, Color.red);

        if (Physics.Raycast(last_destination + to_camera.normalized, to_camera - to_camera.normalized, out hit, (to_camera - to_camera.normalized * 2f).magnitude))
        {
            Debug.Log(hit.collider.gameObject.name);
            if (hit.collider.gameObject.name != "Player")
            {
                transform.position = hit.point - to_camera.normalized*0.1f;
            }
        }
    }
}
