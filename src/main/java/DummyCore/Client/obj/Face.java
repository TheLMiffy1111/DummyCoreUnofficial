package DummyCore.Client.obj;

import DummyCore.Utils.TessellatorWrapper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Face
{
	public Vertex[] vertices;
	public Vertex[] vertexNormals;
	public Vertex faceNormal;
	public TextureCoordinate[] textureCoordinates;

	@SideOnly(Side.CLIENT)
	public void addFaceForRender(Tessellator tessellator)
	{
		addFaceForRender(tessellator, 0.0005F);
	}

	@SideOnly(Side.CLIENT)
	public void addFaceForRender(Tessellator tessellator, float textureOffset)
	{
		if (faceNormal == null)
		{
			faceNormal = this.calculateFaceNormal();
		}

		//tessellator.getWorldRenderer().putNormal(faceNormal.x, faceNormal.y, faceNormal.z);

		float averageU = 0F;
		float averageV = 0F;

		if (textureCoordinates != null && textureCoordinates.length > 0)
		{
			for (TextureCoordinate textureCoordinate : textureCoordinates) {
				averageU += textureCoordinate.u;
				averageV += textureCoordinate.v;
			}

			averageU = averageU / textureCoordinates.length;
			averageV = averageV / textureCoordinates.length;
		}

		float offsetU, offsetV;

		for (int i = 0; i < vertices.length; ++i)
		{

			if (textureCoordinates != null && textureCoordinates.length > 0)
			{
				offsetU = textureOffset;
				offsetV = textureOffset;

				if (textureCoordinates[i].u > averageU)
				{
					offsetU = -offsetU;
				}
				if (textureCoordinates[i].v > averageV)
				{
					offsetV = -offsetV;
				}

				TessellatorWrapper.getInstance().addVertexWithUV(vertices[i].x, vertices[i].y, vertices[i].z, textureCoordinates[i].u + offsetU, textureCoordinates[i].v + offsetV);
			}
			else
			{
				TessellatorWrapper.getInstance().addVertex(vertices[i].x, vertices[i].y, vertices[i].z);
			}
		}
	}

	public Vertex calculateFaceNormal()
	{
		Vec3d v1 = new Vec3d(vertices[1].x - vertices[0].x, vertices[1].y - vertices[0].y, vertices[1].z - vertices[0].z);
		Vec3d v2 = new Vec3d(vertices[2].x - vertices[0].x, vertices[2].y - vertices[0].y, vertices[2].z - vertices[0].z);
		Vec3d normalVector = null;

		normalVector = v1.crossProduct(v2).normalize();

		return new Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
	}
}
