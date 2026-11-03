package fr.edouwarden.space.resources.effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Random;

public class ColonneEffect extends Effect{
        public Particle particle;
        public float radius;
        public float height;
        public double angularVelocityX;
        public double angularVelocityY;
        public double angularVelocityZ;
        public double rotationX;
        public double rotationY;
        public double rotationZ;
        public int particles;
        public boolean enableRotation;
        public boolean solid;
        protected int step;
        protected float sideRatio;
        public boolean orient;

        public ColonneEffect(EffectManager effectManager) {
            super(effectManager);
            this.particle = Particle.FLAME;
            this.radius = 1.0F;
            this.height = 3.0F;
            this.angularVelocityX = 0.015707963267948967;
            this.angularVelocityY = 0.018479956785822312;
            this.angularVelocityZ = 0.02026833970057931;
            this.particles = 100;
            this.enableRotation = true;
            this.solid = false;
            this.step = 0;
            this.sideRatio = 0.0F;
            this.orient = false;
            this.type = EffectType.REPEATING;
            this.period = 2;
            this.iterations = 200;
        }

        public void reset() {
            this.step = 0;
        }

        public void onRun() {
            Location location = this.getLocation();
            if (this.sideRatio == 0.0F) {
                this.calculateSideRatio();
            }

            Random r = RandomUtils.random;
            double xRotation = this.rotationX;
            double yRotation = this.rotationY;
            double zRotation = this.rotationZ;
            if (this.orient) {
                xRotation = Math.toRadians((double)(90.0F - location.getPitch())) + this.rotationX;
                yRotation = Math.toRadians((double)(180.0F - location.getYaw())) + this.rotationY;
            }

            if (this.enableRotation) {
                xRotation += (double)this.step * this.angularVelocityX;
                yRotation += (double)this.step * this.angularVelocityY;
                zRotation += (double)this.step * this.angularVelocityZ;
            }

            for(int i = 0; i < this.particles; ++i) {
                float multi = this.solid ? r.nextFloat() : 1.0F;
                Vector v = RandomUtils.getRandomCircleVector().multiply(this.radius);
                if (r.nextFloat() <= this.sideRatio) {
                    v.multiply(multi);
                    v.setY((r.nextFloat() * 2.0F - 1.0F) * (this.height / 2.0F));
                } else {
                    v.multiply(r.nextFloat());
                    if ((double)r.nextFloat() < 0.5) {
                        v.setY(multi * (this.height / 10));
                    } else {
                        v.setY(-multi * (this.height / 2.0F));
                    }
                }

                if (this.enableRotation || this.orient) {
                    VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
                }

                this.display(this.particle, location.add(v));
                location.subtract(v);
            }

            this.display(this.particle, location);
            ++this.step;
        }

        protected void calculateSideRatio() {
            float grounds = 9.869605F * this.radius * 2.0F;
            float side = 6.2831855F * this.radius * this.height;
            this.sideRatio = side / (side + grounds);

        }
    }



