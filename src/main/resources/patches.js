// Initializes coremod
function initializeCoremod() {
    Opcodes = Java.type('org.objectweb.asm.Opcodes');
    ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
    FieldNode = Java.type('org.objectweb.asm.tree.FieldNode');
    MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
    AbstractInsnNode = Java.type('org.objectweb.asm.tree.AbstractInsnNode');
    InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');

    ACC_PUBLIC = Opcodes.ACC_PUBLIC;
    INVOKESTATIC = Opcodes.INVOKESTATIC;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    ALOAD = Opcodes.ALOAD;
    ILOAD = Opcodes.ILOAD;
    FLOAD = Opcodes.FLOAD;
    DLOAD = Opcodes.DLOAD;
    ISTORE = Opcodes.ISTORE;
    RETURN = Opcodes.RETURN;
    ARETURN = Opcodes.ARETURN;
    IRETURN = Opcodes.IRETURN;
    DRETURN = Opcodes.DRETURN;
    NEW = Opcodes.NEW;
    ACONST_NULL = Opcodes.ACONST_NULL;
    ICONST_0 = Opcodes.ICONST_0;
    IFEQ = Opcodes.IFEQ;
    IFNE = Opcodes.IFNE;
    IF_ACMPEQ = Opcodes.IF_ACMPEQ;
    GETFIELD = Opcodes.GETFIELD;
    GETSTATIC = Opcodes.GETSTATIC;
    GOTO = Opcodes.GOTO;
    LABEL = AbstractInsnNode.LABEL;
    METHOD_INSN = AbstractInsnNode.METHOD_INSN;

    return {
        'attackDelayPatch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.player.PlayerEntity',
                'methodName': 'func_184818_cX',
                'methodDesc': '()F'
            },
            'transformer': function (methodNode) {
                /*
                public getCurrentItemAttackStrengthDelay()F
                   L0
                    LINENUMBER 1907 L0
                    DCONST_1
                    ALOAD 0
                    GETSTATIC net/minecraft/entity/ai/attributes/Attributes.ATTACK_SPEED : Lnet/minecraft/entity/ai/attributes/Attribute;        // remove
                    INVOKEVIRTUAL net/minecraft/entity/player/PlayerEntity.getAttributeValue (Lnet/minecraft/entity/ai/attributes/Attribute;)D   // remove

                    ====== PATCH START ======
                    INVOKESTATIC dev/toma/gunsrpg/asm/Hooks.modifyAttackDelay (Lnet/minecraft/entity/player/PlayerEntity;)D
                    ====== PATCH END   ======

                    DDIV
                    LDC 20.0
                    DMUL
                    D2F
                    FRETURN
                   L1
                    LOCALVARIABLE this Lnet/minecraft/entity/player/PlayerEntity; L0 L1 0
                    MAXSTACK = 4
                    MAXLOCALS = 1
                */
                let instructions = methodNode.instructions;
                for (let i = 0; i < instructions.size(); i++) {
                    let instruction = instructions.get(i);
                    if (instruction.getOpcode() === GETSTATIC) {
                        let virtualInvoke = instructions.get(i + 1);
                        let list = new InsnList();
                        list.add(new MethodInsnNode(
                            INVOKESTATIC,
                            'dev/toma/gunsrpg/asm/Hooks',
                            'modifyAttackDelay',
                            '(Lnet/minecraft/entity/player/PlayerEntity;)D',
                            false
                        ));
                        instructions.insertBefore(instruction, list);
                        instructions.remove(instruction);
                        instructions.remove(virtualInvoke);
                        break;
                    }
                }
                return methodNode;
            }
        },

        'followDistancePatch': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.ai.goal.TargetGoal',
                'methodName': 'func_111175_f',
                'methodDesc': '()D'
            },
            'transformer': function (methodNode) {
                /*
                protected getFollowDistance()D
                   L0
                    LINENUMBER 74 L0
                    ALOAD 0

                    ===== PATCH START =====
                    INVOKESTATIC dev/toma/gunsrpg/asm/Hooks.modifyFollowDistance (Lnet/minecraft/entity/MobEntity;)D
                    ===== PATCH END   =====

                    GETFIELD net/minecraft/entity/ai/goal/TargetGoal.mob : Lnet/minecraft/entity/MobEntity;                                 // remove
                    GETSTATIC net/minecraft/entity/ai/attributes/Attributes.FOLLOW_RANGE : Lnet/minecraft/entity/ai/attributes/Attribute;   // remove
                    INVOKEVIRTUAL net/minecraft/entity/MobEntity.getAttributeValue (Lnet/minecraft/entity/ai/attributes/Attribute;)D        // remove
                    DRETURN
                   L1
                    LOCALVARIABLE this Lnet/minecraft/entity/ai/goal/TargetGoal; L0 L1 0
                    MAXSTACK = 2
                    MAXLOCALS = 1
                */
                let instructions = methodNode.instructions;
                for (let i = 0; i < instructions.size(); i++) {
                    let instruction = instructions.get(i);
                    if (instruction.getOpcode() === ALOAD) {
                        let list = new InsnList();
                        list.add(new FieldInsnNode(
                                GETFIELD,
                                'net/minecraft/entity/ai/goal/TargetGoal',
                                ASMAPI.mapField('field_75299_d'),
                                'net/minecraft/entity/MobEntity'
                        ));
                        list.add(new MethodInsnNode(
                                INVOKESTATIC,
                                'dev/toma/gunsrpg/asm/Hooks',
                                'modifyFollowDistance',
                                '(Lnet/minecraft/entity/MobEntity;)D',
                                false
                        ));
                        let getFieldInsn = instructions.get(i + 1);
                        let getStaticInsn = instructions.get(i + 2);
                        let invokeVirtualInsn = instructions.get(i + 3);
                        instructions.insert(instruction, list);
                        instructions.remove(getFieldInsn);
                        instructions.remove(getStaticInsn);
                        instructions.remove(invokeVirtualInsn);
                        break;
                    }
                }
                return methodNode;
            }
        }
    }
}