using UnityEngine;
using System.Collections;

public class LoopingColour : MonoBehaviour {

    [Range(0.0f, 10.0f)]
    public float speed;

    [Range(0.0f, 1.0f)]
    public float max_brightness, min_brightness;

    private float r_track, b_track, g_track;
    private Camera cam;

	void Start () {
        cam = GetComponent<Camera>();
        r_track = Random.Range(0f, 1f);
        g_track = Random.Range(0f, 1f);
        b_track = Random.Range(0f, 1f);
    }
	
	void FixedUpdate ()
    {
        //Add on set amounts
        r_track += speed * 0.01f;
        b_track += speed * 0.015f;
        g_track += speed * 0.02f;

        float[] RGBA = {
            Mathf.Abs(Mathf.Sin(r_track)),
            Mathf.Abs(Mathf.Sin(g_track)),
            Mathf.Abs(Mathf.Sin(b_track)),
        };

        //Ensure colours are inside of the range
        for (int i = 0; i<RGBA.Length; i++)
        {
            if (RGBA[i] > max_brightness) RGBA[i] = max_brightness;
            if (RGBA[i] < min_brightness) RGBA[i] = min_brightness;
        }


        cam.backgroundColor = new Color(RGBA[0], RGBA[1], RGBA[2]);
   	}
}
